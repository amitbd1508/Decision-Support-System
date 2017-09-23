
<?php

require_once __DIR__.'\DB_Functions.php';

$db = new DB_Functions();

 
// json response array

 
if (isset($_GET['post_id'])) {
    // receiving the post params
    $id = $_GET['post_id'];
    
    $res = $db->deletePost($id);
    echo $res;
       
} else {
    return "0";
}
?>