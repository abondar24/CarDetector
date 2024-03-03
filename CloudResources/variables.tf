variable region {
  default = "eu-west-1"
}

variable "detector_bucket" {
  default = "car-detector-ai"
}

variable "model_file" {
  default = "car_detection_model.keras"
}

variable "model_path" {
  default = "../Model"
}

variable "annotations_path" {
  default = "../Model"
}

variable "annotations_file" {
  default = "_annotations.csv"
}

variable "ecr_repo" {
  default = "cardetector"
}