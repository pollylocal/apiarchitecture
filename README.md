The main difference between Spring RestDocs and Swagger is a test-driven approach to generating API documentation. Thanks to that Spring REST Docs ensures that the documentation is always generated accurately matches the actual behavior of the API.

Also Spring RestDocs provides you with more control over your documentation because of the flexibility of asciidoctor format.

Personally I don't like tons of Swagger annotations just above our controllers methods, they clutter up the controller itself.

To read the documentation
1) run gradlew clean build
2) run java -jar build/libs/apiarchitecture-1.0.jar
3) open http://localhost:8080/docs/api-guide.html in you favourite browser
