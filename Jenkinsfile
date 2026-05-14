pipeline {
    agent any

    stages {
        stage('Fetch Code') {
            steps {
                // Pulls the latest code from your GitHub main branch
                checkout scm
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Rebuilds Spring Boot API image with the latest code
                echo 'Building new API image...'
                sh 'docker build -t expense-tracker-api:latest .'
            }
        }
        
        stage('Deploy Infrastructure') {
            steps {
                // Safely tears down the old containers and spins up the new ones
                echo 'Deploying to Docker...'
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }
    }
}

//Testing Webhook