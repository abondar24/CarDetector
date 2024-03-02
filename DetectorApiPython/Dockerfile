FROM python:3.11

#RUN apk add --no-cache gcc g++

# Set the working directory
WORKDIR /app

# Copy requirements.txt
COPY requirements-docker.txt ./requirements.txt

# Install dependencies from requirements.txt
RUN pip install -r requirements.txt

# Copy the application code
COPY . .

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}

# Expose the port where the API will listen
EXPOSE 8000

# Entrypoint command to run the API
CMD ["uvicorn", "detector_api:app", "--host", "0.0.0.0", "--port", "8000"]