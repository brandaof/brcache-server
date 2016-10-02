prunsrv install BRCache10B3 ^
--DisplayName="BRCache Server Beta" ^
--Description="The BRCache is a general-purpose caching system with transaction support." ^
--Install=C:\projetos\brcache\brcache-server\install\prunsrv.exe ^
--StartPath=C:\brcache ^
--Classpath="C:\brcache\brcache-server-1.0-b3.jar";"C:\brcache\lib\brcache-1.0-b3.jar";"C:\brcache\lib\named-lock-1.0-b2.jar" ^
--StartMode=jvm ^
--StartClass=org.brandao.brcache.server.Bootstrap ^
--StartMethod=start ^
--StartParams=--default-file=C:\brcache\brcache.conf ^
--StopPath=C:\brcache ^
--StopMode=jvm ^
--StopClass=org.brandao.brcache.server.Bootstrap ^
--StopMethod=stop ^
--StopParams= ^
--StdOutput=stdout.log --StdError=stderr.log ^
--Startup=auto --JvmOptions=-Xms128m ^
++JvmOptions=-Xmx128m