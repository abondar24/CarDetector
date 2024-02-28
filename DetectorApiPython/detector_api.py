from fastapi import FastAPI, UploadFile

from detector_service import classify_image
from s3_client import download_data_from_bucket

model_data, annotations_df = download_data_from_bucket()

app = FastAPI()


@app.get("/health")
async def root():
    return {"message": "API is up"}


@app.post("/detector")
async def detector(image: UploadFile):
    try:
        content = await image.read()
        with open(image.filename, "wb") as buffer:
            buffer.write(content)

        car_model = classify_image(buffer, model_data, annotations_df)

    except Exception:
        return {"message": "There was an error uploading the file"}

    finally:
        image.file.close()
    return {"carModel": f"{car_model}"}
