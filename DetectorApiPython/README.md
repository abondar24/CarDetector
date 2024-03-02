# Detector API python

Python version of car detector api

## Build and run

- install required dependencies
```
pip install -r requirements.txt
```
- run app locally
```
 uvicorn detector_api:app --reload
```

### Docker run
```
 docker build -t cardetector .
 
 docker run -itd -p 8000:8000 --name ss-213 -e=AWS_ACCESS_KEY_ID=key_id -e=AWS_ACCESS_KEY=key  cardetector 
```

## API reference
Swagger UI available here: localhost:8000/docs
