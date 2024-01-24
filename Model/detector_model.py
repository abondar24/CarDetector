import os.path

import pandas as pd

import tensorflow as tf
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from tensorflow.keras.models import Sequential
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.optimizers.legacy import Adam as AdamLegacy

TRAIN_DIR = "Data/train"
TEST_DIR = "Data/test"
VALID_DIR = "Data/valid"

ANNOTATIONS = "/_annotations.csv"
ANNOTATIONS_TRAIN = TRAIN_DIR+ANNOTATIONS
ANNOTATIONS_TEST = TEST_DIR+ANNOTATIONS
ANNOTATIONS_VALID = VALID_DIR+ANNOTATIONS


IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS = 640, 640, 3
IMAGE_SIZE = (IMAGE_WIDTH, IMAGE_HEIGHT)
FAST_RUN = False


def make_dataset(row):
    img = tf.io.read_file(os.path.join(TRAIN_DIR, row))
    img = tf.image.decode_jpeg(img, channels=3)
    return img


def create_model(shape, num_classes):
    train_model = Sequential()
    train_model.add(Conv2D(16, (3, 3), activation='relu', input_shape=shape))
    train_model.add(MaxPooling2D(2, 2))
    train_model.add(Conv2D(32, (3, 3), activation='relu'))
    train_model.add(MaxPooling2D(2, 2))
    train_model.add(Flatten())
    train_model.add(Dense(128, activation='relu'))
    train_model.add(Dropout(0.5))
    train_model.add(Dense(num_classes, activation='softmax'))

    train_model.summary()

    return train_model


train_data_frame = pd.read_csv(ANNOTATIONS_TRAIN)
test_data_frame = pd.read_csv(ANNOTATIONS_TEST)
valid_data_frame = pd.read_csv(ANNOTATIONS_VALID)

train_datagen = ImageDataGenerator(rescale=1. / 255,
                                   shear_range=0.2,
                                   zoom_range=0.2,
                                   horizontal_flip=True,
                                   validation_split=0.2)

train_generator = train_datagen.flow_from_dataframe(dataframe=train_data_frame,
                                                    directory=TRAIN_DIR,
                                                    x_col='filename',
                                                    y_col='class',
                                                    target_size=IMAGE_SIZE,
                                                    batch_size=32,
                                                    class_mode='categorical',
                                                    subset='training')

test_datagen = ImageDataGenerator(rescale=1. / 255)
test_generator = test_datagen.flow_from_dataframe(dataframe=test_data_frame,
                                                  directory=TEST_DIR,
                                                  x_col='filename',
                                                  y_col='class',
                                                  target_size=IMAGE_SIZE,
                                                  batch_size=32,
                                                  class_mode='categorical')

val_generator = train_datagen.flow_from_dataframe(dataframe=valid_data_frame,
                                                  directory=TRAIN_DIR,
                                                  x_col='filename',
                                                  y_col='class',
                                                  target_size=IMAGE_SIZE,
                                                  batch_size=32,
                                                  class_mode='categorical',
                                                  subset='validation')


# Create the model
input_shape = (IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_CHANNELS)
model = create_model(input_shape, train_data_frame['class'].nunique())

# Compile the model
model.compile(optimizer=AdamLegacy(learning_rate=0.0001),
              loss='mean_squared_error',
              metrics=['accuracy'])

# Train the model
history = model.fit(train_generator,
                    steps_per_epoch=train_generator.samples // train_generator.batch_size,
                    epochs=20,  # Adjust the number of epochs as needed
                    validation_data=val_generator,
                    validation_steps=val_generator.samples // val_generator.batch_size)

#Test the mode;
test_loss, test_accuracy = model.evaluate(test_generator, steps=test_generator.samples // test_generator.batch_size)
print(f"Test Loss: {test_loss}, Test Accuracy: {test_accuracy}")

# Save the model
model.save("car_detection_model.keras")
