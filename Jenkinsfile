pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=true"
        DOCKER_IMAGE = "your-ecr-repo-name" // TODO: Update this with your actual ECR repository
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

        stage('Docker Build') {
            when {
                expression { return false } // TODO: Change to 'true' when ready
            }
            steps {
                echo 'Building Docker image...'
                // Example:
                // script {
                //     docker.build("${DOCKER_IMAGE}:${VERSION}", "./demo")
                // }
            }
        }

        stage('Trivy Scan') {
            when {
                expression { return false } // TODO: Change to 'true' when ready
            }
            steps {
                echo 'Scanning Docker image...'
                // Example:
                // sh "trivy image ${DOCKER_IMAGE}:${VERSION}"
            }
        }

        stage('Push to ECR') {
            when {
                expression { return false } // TODO: Change to 'true' when ready
            }
            steps {
                echo 'Pushing image to AWS ECR...'
                // Example:
                // script {
                //     docker.withRegistry("https://${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com", 'ecr:aws-credentials-id') {
                //         docker.image("${DOCKER_IMAGE}:${VERSION}").push()
                //     }
                // }
            }
        }

        stage('Deploy to EKS') {
            when {
                expression { return false } // TODO: Change to 'true' when ready
            }
            steps {
                echo 'Deploying to EKS using Helm...'
                // Example:
                // sh "helm upgrade --install my-app ./helm-chart --set image.tag=${VERSION}"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
