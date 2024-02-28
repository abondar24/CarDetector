from fastapi import FastAPI, UploadFile, HTTPException
from fastapi.responses import JSONResponse
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


    finally:
        image.file.close()
    return {"carModel": f"{car_model}"}


@app.exception_handler(ConnectionError)
async def connection_error_handler(exc: ConnectionError):
    logger.error(f"Error downloading model: {exc}")
    return JSONResponse(
        status_code=400,
        content={"message": f"OError downloading model: {exc}"},
    )


@app.exception_handler(HTTPException)
async def http_exception_handler(exc: HTTPException):
    logger.error(f"Error downloading model: {exc}")
    return JSONResponse(
        status_code=600,
        content={"message": f"OInternal server error: {exc}"},
    )
