pipeline {
    agent any
    tools {
        maven 'apache-maven-latest'
        jdk 'adoptopenjdk-hotspot-jdk8-latest'
    }
    stages {
        stage('Build') {
            steps {
                wrap([$class: 'Xvfb', autoDisplayName: true, debug: true, parallelBuild: true, screen: '1024x758x16']) {
		    sh '''
			metacity --sm-disable --replace &
                        mvn -f org.eclipse.swtchart.cbi/pom.xml clean install
                    '''
		}
            }
        }
    }
}
