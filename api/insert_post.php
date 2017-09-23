
<?php

require_once __DIR__.'\DB_Functions.php';

$db = new DB_Functions();

 
// json response array
$response = array("error" => FALSE);
 
if (isset($_GET['title']) && isset($_GET['body']) && isset($_GET['submittedby']) && isset($_GET['category'])) {
 
    // receiving the post params
    $title = $_GET['title'];
    $body = $_GET['body'];
    $submittedby = $_GET['submittedby'];
    $category = $_GET['category'];
    // check if user is already existed with the same email
    
    
    $res = $db->insertPost($title,$body,$submittedby,$category);
    echo $res;
    
    
} else {
    return "0";
}
?>