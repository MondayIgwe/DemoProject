pipeline{
	agent any
environment{
		NEW_VERSION = '1.3.0'
		PATH = "C:/apache-maven-3.9.9/bin"
	}
    stages{
		stage('Build'){
			steps{
				echo 'Building the project'
                sh 'mvn clean compile'
                echo "Version: ${NEW_VERSION}"
            }
        }

    }