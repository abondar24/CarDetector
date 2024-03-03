resource "aws_ecr_repository" "car_detector" {
  name                 = var.ecr_repo
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

