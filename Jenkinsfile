pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=true"
        DOCKER_IMAGE = "your-ecr-repo-name" // We'll update this in Step 10
        VERSION = "1.0.${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                dir('demo') {
                   sh 'mvn clean install -DskipTests'
        }
            }
        }

        stage('Unit Tests') {
            steps {
                dir('demo') {
                sh 'mvn test'
        }

            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { return false } // Enable in Step 8
            }
            steps {
                // Placeholder - We'll configure this later
                echo 'Running SonarQube...'
            }
        }

        stage('Docker Build') {
            when {
                expression { return false } // Enable in Step 9
            }
            steps {
                echo 'Building Docker image...'
            }
        }

        stage('Trivy Scan') {
            when {
                expression { return false } // Enable in Step 9
            }
            steps {
                echo 'Scanning Docker image...'
            }
        }

        stage('Push to ECR') {
            when {
                expression { return false } // Enable in Step 10
            }
            steps {
                echo 'Pushing image to AWS ECR...'
            }
        }

        stage('Deploy to EKS') {
            when {
                expression { return false } // Enable in Step 11
            }
            steps {
                echo 'Deploying to EKS using Helm...'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
