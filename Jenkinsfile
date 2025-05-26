pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=true"
        DOCKER_IMAGE = "032008616903.dkr.ecr.eu-north-1.amazonaws.com/jenkins"
        VERSION = "1.0.${BUILD_NUMBER}"
        AWS_REGION = "eu-north-1"
        ECR_REGISTRY = "032008616903.dkr.ecr.${AWS_REGION}.amazonaws.com"
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
                echo 'Running SonarQube Analysis...'
                withCredentials([string(credentialsId: 'sonar-token-id', variable: 'SONAR_AUTH_TOKEN')]) {
                    withSonarQubeEnv('SonarQube') {
                        dir('demo') {
                            sh """
                                mvn sonar:sonar \
                                    -Dsonar.login=${SONAR_AUTH_TOKEN} \
                                    -Dsonar.projectKey=demo-project \
                                    -Dsonar.host.url=http://sonarqube:9000
                            """
                        }
                    }
                }
            }
        }

        stage('Debug') {
            steps {
                sh 'pwd && ls -l'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                script {
                    docker.build("${DOCKER_IMAGE}:${VERSION}")
                }
            }
        }

        stage('Trivy Scan') {
            steps {
                echo 'Scanning Docker image...'
                sh "trivy image ${DOCKER_IMAGE}:${VERSION} || true"
            }
        }

        stage('AWS ECR Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'aws-ecr-creds', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh '''
                      aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                      aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                      aws configure set default.region ${AWS_REGION}
                      aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                    '''
                }
            }
        }

        stage('Push to ECR') {
            steps {
                echo 'Pushing image to AWS ECR...'
                sh "docker push ${DOCKER_IMAGE}:${VERSION}"
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
