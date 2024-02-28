import io
import os

import boto3
import pandas as pd

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
        response = s3_client.get_object(Bucket=aws_bucket, Key=detector_model)
        print("Downloading model")
        with response['Body'] as data:
            model = data.read()

        response = s3_client.get_object(Bucket=aws_bucket, Key=detector_annotations)
        print("Downloading annotations")
        with response['Body'] as data:
            annotation_data = io.BytesIO(data.read())
            annotation_df = pd.read_csv(annotation_data)

        print(f"Downloaded file '{detector_model}' successfully!")
        print(f"Downloaded and converted file '{detector_annotations}' to pandas DataFrame successfully!")
        return model, annotation_df

    except ConnectionError as e:
        print(f"Error downloading data: {e}")
        return None, None
