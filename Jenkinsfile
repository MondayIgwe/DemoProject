pipeline {
    // 1. Agent Selection: Run on any available agent
    agent any

    // 2. Tools: Define JDK and Maven versions configured in Jenkins Global Tool Config
    //    --> IMPORTANT: Replace 'JDK 11' and 'Maven 3.9.x' with the EXACT names
    //        you configured in Manage Jenkins -> Global Tool Configuration.
    tools {
        jdk 'JAVA_HOME'     // Use the name of your configured JDK Jenkins
        maven 'MAVEN_HOME' // Use the name of your configured Maven installation in Jenkins
    }

    // 3. Environment Variables (Optional)
    environment {
        // You can define project-specific variables here
        NEW_VERSION = '1.3.0'
        // NOTE: Setting PATH here is generally discouraged. Use the 'tools' directive instead.
        // PATH = "C:/apache-maven-3.9.9/bin:${env.PATH}" // <-- Avoid this if possible
    }

    // 4. Stages: Define the workflow steps
    stages {
        // Stage 1: Checkout - Get the source code from your SCM (Git, SVN, etc.)
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                // This step automatically checks out from the SCM configured in the Jenkins job
                checkout scm
            }
        }

        // Stage 2: Build - Compile the code and run quick checks
        stage('Build') {
            steps {
                echo "Building the project (Version defined in Jenkinsfile: ${env.NEW_VERSION})..."
                // Use 'mvnw' if your project includes the Maven wrapper (recommended)
                // sh './mvnw clean compile'
                // Otherwise, use the 'mvn' command configured via the 'tools' directive
                bat 'mvn clean compile'
            }
        }

        // Stage 3: Test - Run unit and integration tests
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Use 'mvnw' if you have the wrapper
                // sh './mvnw test'
                // Use 'mvn' configured via 'tools'
                sh 'mvn test'
            }
        }

        // Stage 4: Package (Optional but common) - Create the JAR/WAR file
        stage('Package') {
            steps {
                echo 'Packaging the application...'
                // Use 'mvnw' if you have the wrapper
                // sh './mvnw package'
                // Use 'mvn' configured via 'tools'
                sh 'mvn package'

                // Optional: Archive the build artifact (e.g., the JAR/WAR file)
                // Adjust the path 'target/*.jar' as needed for your project's output
                // archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // Add more stages as needed (e.g., 'Deploy', 'Code Analysis', 'Security Scan')
        /*
        stage('Deploy') {
            when {
                branch 'main' // Only deploy from the main branch, for example
            }
            steps {
                echo 'Deploying...'
                // Add deployment steps here
            }
        }
        */
    }

    // 5. Post Actions: Actions to run after all stages complete
    post {
        // Always runs regardless of pipeline status
        always {
            echo 'Pipeline finished. Archiving test results and cleaning up...'
            // Archive JUnit test results for display in Jenkins UI
            // Assumes standard Maven Surefire report location
            junit 'target/surefire-reports/*.xml'

            // Clean up the workspace after the build
            cleanWs()
        }

        // Runs only if the pipeline succeeds
        success {
            echo 'Pipeline completed successfully!'
            // Add success notifications (e.g., email, Slack) if desired
        }

        // Runs only if the pipeline fails
        failure {
            echo 'Pipeline failed.'
            // Add failure notifications if desired
            // Example using Email Extension plugin (ensure it's configured):
            /*
            emailext (
                subject: "FAILED: Pipeline '${env.JOB_NAME}' (${env.BUILD_NUMBER})",
                body: """<p>FAILED: Pipeline '${env.JOB_NAME}' (${env.BUILD_NUMBER}):</p>
                       <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
                to: 'your-team@example.com' // Replace with actual recipients
            )
            */
        }

        // Runs only if the pipeline status changes (e.g., Failed -> Success)
        // changed {
        //    echo 'Pipeline status changed.'
        // }
    }
}