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

variable "ecs_cluster" {
  default = "car-detector-cluster"
}

variable "ecs_def_family" {
  default = "deploy-detector-definition"
}

variable "ecs_service_name" {
  default = "car-detector-service"
}