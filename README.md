SamlSnort
=========
x64 install example until a better way is found
    mvn install:install-file -P win64,install-swt-64bit
	mvn install:install-file -P win64,install-djnativeswing
	mvn install:install-file -P win64,install-djnativeswing-swt
	mvn install:install-file -P win64,install-syntaxhighlighter
	mvn package -P win64
	
Then check out target for the constructed zip.