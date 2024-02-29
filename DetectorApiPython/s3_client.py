import io
import logging
import os

import boto3
import pandas as pd

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

detector_model = "car_detection_model.keras"
detector_annotations = "_annotations.csv"

aws_bucket = "car-detector-ai"
aws_region = "eu-west-1"
aws_access_key_id = os.environ.get("AWS_ACCESS_KEY_ID")
aws_access_key = os.environ.get("AWS_ACCESS_KEY")

if not all([aws_access_key_id, aws_access_key]):
    raise ValueError("Missing required environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)")

s3_client = boto3.client(
    's3',
    aws_access_key_id=aws_access_key_id,
    aws_secret_access_key=aws_access_key,
    region_name=aws_region
)


def download_data_from_bucket():
    try:
        if not os.path.exists(detector_model):
            response = s3_client.get_object(Bucket=aws_bucket, Key=detector_model)
            logger.info("Downloading model")
            with response['Body'] as data:
                model = data.read()

                with open(detector_model, 'wb') as f:
                    f.write(model)
                    logger.info(f"Downloaded file '{detector_model}' successfully!")
        else:
            logger.info("Skipping model download - already downloaded")

        response = s3_client.get_object(Bucket=aws_bucket, Key=detector_annotations)
        logger.info("Downloading annotations")
        with response['Body'] as data:
            annotation_data = io.BytesIO(data.read())
            annotation_df = pd.read_csv(annotation_data)
            logger.info(f"Downloaded and converted file '{detector_annotations}' to pandas DataFrame successfully!")

        return annotation_df

    except ConnectionError as e:
        logger.info(f"Error downloading data: {e}")
        return None, None
