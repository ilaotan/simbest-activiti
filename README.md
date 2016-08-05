##集成Activiti工作流引擎


#Maven 仓库添加
```
<dependency>
	<groupId>com.simbest</groupId>
	<artifactId>simbest-activiti</artifactId>
	<version>0.5</version>
</dependency>
		
		
<repositories>
	<repository>
		<id>simbest-activiti-mvn-repo</id>
		<url>https://raw.github.com/simbest/simbest-activiti/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>	
```

[查看流程定义](http://localhost:8080/urmp/action/sso/activiti/deployment/processList?username=admin&token=d9c2f2afaa3f5f2453bc688d119b5605)
