from fastapi import FastAPI, UploadFile, HTTPException
import logging
from detector_service import classify_image
from s3_client import download_data_from_bucket

logger = logging.getLogger(__name__)

model_data, annotations_df = download_data_from_bucket()

app = FastAPI(swagger_ui_parameters={"syntaxHighlight": False})


@app.get("/health")
async def root():
    return {"message": "API is up"}


@app.post("/detector")
async def detector(image: UploadFile):
    try:
        content = await image.read()
        car_model = classify_image(content, model_data, annotations_df)

    except UploadFile.errors.ASGIHTTPException as e:
        logger.error(f"Error reading uploaded file: {e}")
        raise HTTPException(status_code=400, detail="Error reading uploaded image")
    except ConnectionError as e:
        logger.error(f"Error downloading model: {e}")
        raise HTTPException(status_code=502, detail="Error reading uploaded image")
    except Exception as e:
        logger.exception(f"Unexpected error: {e}")  # Log full traceback for unexpected errors
        raise HTTPException(status_code=500, detail="Internal server error")


    finally:
        image.file.close()
    return {"carModel": f"{car_model}"}
