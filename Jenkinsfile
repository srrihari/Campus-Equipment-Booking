
pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Package Application') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat '"C:\\Users\\SNUC\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t campus-equipment-booking:set-f .'
            }
        }
        
        
       
        stage('Deploy to Kubernetes') {
            steps {
                bat 'set "MINIKUBE_HOME=C:\\Users\\SNUC" && "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" -p minikube image load campus-equipment-booking:set-f'

                bat '"C:\\Users\\SNUC\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" --kubeconfig="C:\\Users\\SNUC\\.kube\\config" apply -f k8s/deployment.yaml'

                bat '"C:\\Users\\SNUC\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" --kubeconfig="C:\\Users\\SNUC\\.kube\\config" apply -f k8s/service.yaml'

                bat '"C:\\Users\\SNUC\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" --kubeconfig="C:\\Users\\SNUC\\.kube\\config" rollout restart deployment/campus-equipment-booking'

                bat '"C:\\Users\\SNUC\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" --kubeconfig="C:\\Users\\SNUC\\.kube\\config" rollout status deployment/campus-equipment-booking --timeout=120s'
            }
        }



    }

    post {
        success {
            echo 'Build, tests, packaging, Docker image creation, and Kubernetes deployment completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the console output.'
        }
    }
}
