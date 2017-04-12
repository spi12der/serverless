
1. type = createUserDir | createDir | createFile | updateFile | deleteFile | readFile

createUserDir
2. username
returns status, message

createDir
2. username
3. directoryName
returns status, message

createFile
2. username
3. directoryName
4. fileName
returns status, message

updateFile
2. username
3. directoryName
4. fileName
5. content
returns status, message

deleteFile
2. username
3. directoryName
4. fileName
returns status, message

readFile
2. username
3. directoryName
4. fileName
returns fileContent, status, message
