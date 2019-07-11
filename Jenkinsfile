pipeline {
    agent { docker { image 'maven:3.5.0' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn -f /home/jenkins/workspace/SWTChart/org.eclipse.swtchart.cbi/pom.xml clean install'
            }
        }
    }
}
