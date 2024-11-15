import logging

import numpy as np
from keras.applications.inception_v3 import preprocess_input
from keras.models import load_model
from keras.preprocessing import image

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

model_data = "car_detection_model.keras"
image_file = "image.jpg"


def classify_image(annotations_df):
    logger.info("Detecting car model")
    model = load_model(model_data)
    logger.info("Model ready")

    # Load and preprocess the image
    img = image.load_img(image_file, target_size=(640, 640))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)
    img_array = preprocess_input(img_array)
    logger.info("Image ready")

    # Make predictions
    predictions = model.predict(img_array)
    logger.info(predictions)

    predicted_class_index = np.argmax(predictions)
    predicted_class_label = annotations_df.loc[annotations_df.index[predicted_class_index], "class"]
    predicted_probability = predictions[0][predicted_class_index]

    logger.info(f"Predicted Class: {predicted_class_label}, Probability: {predicted_probability:.4f}")

    return predicted_class_label, predicted_probability

