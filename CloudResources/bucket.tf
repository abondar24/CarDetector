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
  key    = var.annotations_file
  source = "${var.annotations_path}/${var.annotations_file}"
}

resource "aws_s3_bucket_public_access_block" "dt_acl_block" {
  bucket = aws_s3_bucket.detector_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_ownership_controls" "dt_bucket_ownership" {
  bucket = aws_s3_bucket.detector_bucket.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_acl" "dt_acl" {
  depends_on = [
    aws_s3_bucket_ownership_controls.dt_bucket_ownership,
    aws_s3_bucket_public_access_block.dt_acl_block]
  bucket = aws_s3_bucket.detector_bucket.id
  acl    = "public-read"
}

resource "aws_s3_bucket_policy" "detector_bucket_policy" {
  bucket = aws_s3_bucket.detector_bucket.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Sid       = "PublicReadGetObject",
        Effect    = "Allow",
        Principal = "*",
        Action    = "s3:GetObject",
        Resource  = "${aws_s3_bucket.detector_bucket.arn}/*",  # Corrected the closing quotation mark and used interpolation syntax
        Condition = {
          StringEquals = {
            "aws:sourceVpce" = "*"
          }
        }
      }
    ]
  })
}
