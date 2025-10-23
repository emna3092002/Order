pipeline {
  agent any

  environment {
    IMAGE_NAME = "emna239/order-app"
    MVN_OPTS = "-DskipTests=false"
  }

  tools {
    maven 'Maven3'   // nom tel que configuré dans Global Tool Configuration
    jdk 'jdk22'      // nom tel que configuré
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        // Windows: use bat, Linux agents would use sh
        bat "mvn clean package ${MVN_OPTS}"
      }
      post {
        success {
          archiveArtifacts artifacts: "target/*.jar", fingerprint: true
        }
      }
    }

    stage('Unit tests') {
      steps {
        bat "mvn test"
      }
    }

    stage('Docker Build') {
      steps {
        // build image using Docker CLI available on agent
        // tag with build number for traceability
        bat "docker build -t ${env.IMAGE_NAME}:${env.BUILD_NUMBER} ."
      }
    }

    stage('Push to Registry (optional)') {
      when {
        expression { return env.DOCKER_PUSH == 'true' }
      }
      steps {
        // Use Jenkins credentials - create them and reference id "docker-hub-cred"
        withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          bat """
            echo Logging in...
            docker login -u %DOCKER_USER% -p %DOCKER_PASS%
            docker push ${env.IMAGE_NAME}:${env.BUILD_NUMBER}
          """
        }
      }
    }
  }

  post {
    success {
      echo "Build succeeded: ${env.BUILD_NUMBER}"
    }
    failure {
      echo "Build failed!"
    }
  }
}
