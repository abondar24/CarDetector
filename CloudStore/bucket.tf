resource "aws_s3_bucket" "detector_bucket" {
  bucket = var.detector_bucket
  tags = {
    Name = "Car Detector data bucket"
    Environment = "dev"
  }

}


resource "aws_s3_object" "model_object" {
  bucket = aws_s3_bucket.detector_bucket.id
  key    = var.model_file
  source = "${var.model_path}/${var.model_file}"
}

resource "aws_s3_object" "annotation_object" {
  bucket = aws_s3_bucket.detector_bucket.id
  key    = var.detector_bucket
  source = "${var.model_path}/${var.model_file}"
}