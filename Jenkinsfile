pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=true"
        DOCKER_IMAGE = "your-ecr-repo-name" // update later
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
            steps {
                script {
                    echo 'Starting SonarQube Analysis...'
                    withSonarQubeEnv('sonar-token-id') {
                        dir('demo') {
                            def status = sh(script: 'mvn sonar:sonar', returnStatus: true)
                            if (status != 0) {
                                error "SonarQube analysis failed"
                            }
                        }
                    }
                }
            }
        }

        stage('Docker Build') {
            when {
                expression { return false } // Enable later
            }
            steps {
                echo 'Building Docker image...'
            }
        }

        stage('Trivy Scan') {
            when {
                expression { return false } // Enable later
            }
            steps {
                echo 'Scanning Docker image...'
            }
        }

        stage('Push to ECR') {
            when {
                expression { return false } // Enable later
            }
            steps {
                echo 'Pushing image to AWS ECR...'
            }
        }

        stage('Deploy to EKS') {
            when {
                expression { return false } // Enable later
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
