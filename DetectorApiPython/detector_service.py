import numpy as np
from keras.applications.inception_v3 import preprocess_input, decode_predictions
from keras.models import load_model
from keras.preprocessing import image


def classify_image(image_file, model, annotations_df):
    print("Detecting car model")
    model = load_model(model)

    # Load and preprocess the image
    img = image.load_img(image_file, target_size=(640, 640))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)
    img_array = preprocess_input(img_array)

    # Make predictions
    predictions = model.predict(img_array)
    print(predictions)

    predicted_class_index = np.argmax(predictions)
    class_label = annotations_df.loc[annotations_df.index[predicted_class_index], "class"]
    print("Car model:", class_label)

    predicted_class_index = np.argmax(predictions)
    print("Predicted Probability:", predictions[0][predicted_class_index])
    return predictions[0][predicted_class_index]
