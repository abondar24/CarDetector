import sys

import pandas as pd
import numpy as np
from keras.applications.inception_v3 import preprocess_input, decode_predictions
from keras.models import load_model
from keras.preprocessing import image

model_path = "car_detection_model.keras"
model = load_model(model_path)
df = pd.read_csv("Data/train/_annotations.csv")


def classify_image(image_path):
    # Load and preprocess the image
    img = image.load_img(image_path, target_size=(640, 640))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)
    img_array = preprocess_input(img_array)

    # Make predictions
    predictions = model.predict(img_array)
    print(predictions)

    predicted_class_index = np.argmax(predictions)
    class_label = df.loc[df.index[predicted_class_index], "class"]
    print("Car model:", class_label)

    predicted_class_index = np.argmax(predictions)
    print("Predicted Probability:", predictions[0][predicted_class_index])


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py /path/to/image.jpg")
        sys.exit(1)

    image_path = sys.argv[1]
    classify_image(image_path)
