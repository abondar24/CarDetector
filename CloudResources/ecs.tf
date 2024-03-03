resource "aws_ecs_cluster" "car_detector_cluster" {
  name = var.ecs_cluster
}

resource "aws_ecs_task_definition" "my_task_definition" {
  family =  var.ecs_def_family
  cpu    = "256"
  memory = "8192"

  container_definitions = <<EOF
[
  {
    "name": "detector",
    "image": "203212890819.dkr.ecr.eu-west-1.amazonaws.com/cardetector:latest",
    "essential": true,
    "portMappings": [
      {
        "containerPort": 8000,
        "hostPort": 8000
      }
    ]
  }
]
EOF
}


resource "aws_ecs_service" "detector_service" {
  name = var.ecs_service_name
  cluster = aws_ecs_cluster.car_detector_cluster.arn
  task_definition = aws_ecs_task_definition.my_task_definition.arn
  launch_type = "EXTERNAL"
  desired_count = 1
}