pipeline {
    agent any
    tools {
        maven 'apache-maven-latest'
        jdk 'adoptopenjdk-hotspot-jdk8-latest'
    }
    stages {
        stage('Build') {
            steps {
                sh '''
		    pwd
                    mvn -f org.eclipse.swtchart.cbi/pom.xml clean install
                '''
            }
        }
    }
}
