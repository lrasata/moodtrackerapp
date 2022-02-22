pipeline {
    agent any

    stages {
        stage('Check java'){
            steps {
                sh "java -version"    
            }
            
        }
        stage('clean'){
            steps {
                // Get some code from a GitHub repository
                git branch: "main", url: 'https://github.com/lrasata/moodtrackerapp'
                
                // mvn clean
                sh "./mvnw -ntp clean -P-webapp"    
            }
            
        }
        stage('nohttp') {
            steps {
                sh "./mvnw -ntp checkstyle:check"
            }
        }

        stage('install tools') {
            steps {
                sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm@install-node-and-npm"
            }
        }

        stage('npm install') {
            steps {
                sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"    
            }
            
        }
        stage('backend tests') {
            steps {
                // sh "./mvnw -ntp verify -P-webapp"
                sh "mvn test"
            }
        }
        // No frontend tests written
        // stage('frontend tests'){
            // steps {
                // sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='run test'"
            // }
        // }
        stage('packaging'){
            steps {
                sh "./mvnw -ntp verify -P-webapp -Pprod -DskipTests"    
            }
        }
    }
    post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
        success {
            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }
}
