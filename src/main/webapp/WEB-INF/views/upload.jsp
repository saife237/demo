<!DOCTYPE html>
<html>
<head>
    <title>PDF Upload</title>
</head>
<body>
    <h2>Upload PDF Files</h2>
    <form method="post" enctype="multipart/form-data" action="/upload">
        <label for="path">Destination Path:</label>
        <input type="text" name="path" required/><br/><br/>
        <label for="files">Choose PDF files:</label>
        <input type="file" name="files" accept="application/pdf" multiple required/><br/><br/>
        <input type="submit" value="Upload and Extract Text"/>
    </form>
    <p>${message}</p>
    <pre>${text}</pre>
</body>
</html>
