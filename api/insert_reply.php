
<?php

 
require_once __DIR__.'\DB_Functions.php';

$db = new DB_Functions();

 
if (isset($_GET['post_id']) && isset($_GET['reply'])) {
 
    // receiving the post params
    
    $post_id = $_GET['post_id'];
    $reply = $_GET['reply'];
    
    // check if user is already existed with the same email
    
    
    $res = $db->insertReplay($post_id,$reply);
    echo $res;
    
    
    
} else {
    return "0";
}

?>