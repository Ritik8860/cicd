pipeline {
    agent any

    tools {
        // Ensure 'Maven3' is configured in Jenkins Global Tool Configuration
        maven 'Maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=true"
        DOCKER_IMAGE = "your-ecr-repo-name" // Update this later with your ECR repository name
        VERSION = "1.0.${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checks out the source code from the configured SCM (e.g., Git)
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                // Changes directory to 'demo' (assuming your Maven project is here)
                dir('demo') {
                    // Executes Maven clean install, skipping tests for faster build
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                // Changes directory to 'demo'
                dir('demo') {
                    // Executes Maven unit tests
                    sh 'mvn test'
                }
            }
            post {
                // This 'always' block ensures that JUnit test results are published
                // regardless of whether the 'mvn test' command succeeds or fails.
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis...'
                // Use withCredentials to securely handle the SonarQube token.
                // The 'credentials' function fetches the credential by its ID.
                // The 'SONAR_AUTH_TOKEN' variable will be available only within this block.
                withCredentials([string(credentialsId: 'sonar-token-id', variable: 'SONAR_AUTH_TOKEN')]) {
                    // Enters the SonarQube environment configured in Jenkins.
                    // Ensure 'SonarQube' is configured under Manage Jenkins -> Configure System -> SonarQube servers.
                    withSonarQubeEnv('SonarQube') {
                        // Changes directory to 'demo' where the Maven project is located
                        dir('demo') {
                            // Executes the SonarQube analysis using Maven.
                            // -Dsonar.login: Uses the securely loaded SONAR_AUTH_TOKEN.
                            // -Dsonar.projectKey: Specifies the key for your project in SonarQube.
                            sh "mvn sonar:sonar -Dsonar.login=${SONAR_AUTH_TOKEN} -Dsonar.projectKey=demo-project"
                        }
                    }
                }
            }
        }

        // --- Temporary Stage for Debugging Credentials ---
        // Uncomment this stage if SonarQube Analysis still skips or fails,
        // to verify if the SONAR_AUTH_TOKEN is being loaded correctly.
        /*
        stage('Verify Token (Debug)') {
            steps {
                script {
                    // This will print the token. Be cautious with sensitive info in logs.
                    // For production, avoid printing sensitive data directly.
                    echo "Attempting to load SonarQube Token..."
                    withCredentials([string(credentialsId: 'sonar-token-id', variable: 'VERIFY_TOKEN')]) {
                        echo "Token loaded (first 5 chars): ${VERIFY_TOKEN.substring(0, 5)}..."
                    }
                }
            }
        }
        */

        stage('Docker Build') {
            when {
                // This stage is currently disabled and will be skipped.
                // Change 'return false' to 'return true' to enable it.
                expression { return false } // enable later
            }
            steps {
                echo 'Building Docker image...'
                // Add Docker build commands here, e.g.:
                // script {
                //     docker.build("${DOCKER_IMAGE}:${VERSION}", "./demo")
                // }
            }
        }

        stage('Trivy Scan') {
            when {
                // This stage is currently disabled and will be skipped.
                // Change 'return false' to 'return true' to enable it.
                expression { return false } // enable later
            }
            steps {
                echo 'Scanning Docker image...'
                // Add Trivy scan commands here, e.g.:
                // sh "trivy image ${DOCKER_IMAGE}:${VERSION}"
            }
        }

        stage('Push to ECR') {
            when {
                // This stage is currently disabled and will be skipped.
                // Change 'return false' to 'return true' to enable it.
                expression { return false } // enable later
            }
            steps {
                echo 'Pushing image to AWS ECR...'
                // Add ECR push commands here, e.g.:
                // script {
                //     docker.withRegistry("https://${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com", 'ecr:aws-credentials-id') {
                //         docker.image("${DOCKER_IMAGE}:${VERSION}").push()
                //     }
                // }
            }
        }

        stage('Deploy to EKS') {
            when {
                // This stage is currently disabled and will be skipped.
                // Change 'return false' to 'return true' to enable it.
                expression { return false } // enable later
            }
            steps {
                echo 'Deploying to EKS using Helm...'
                // Add EKS deployment commands here, e.g.:
                // sh "helm upgrade --install my-app ./helm-chart --set image.tag=${VERSION}"
            }
        }
    }

    post {
        // This 'always' block ensures that the workspace is cleaned up
        // after every pipeline run, regardless of success or failure.
        always {
            cleanWs()
        }
    }
}
