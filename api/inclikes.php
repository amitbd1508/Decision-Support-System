
<?php

require_once __DIR__.'\DB_Functions.php';

$db = new DB_Functions();

 
// json response array
$response = array("error" => FALSE);
 
if (isset($_GET['id']) &&isset($_GET['user_id'])) {
 
    // receiving the post params
    $id = $_GET['id'];
    $user_id = $_GET['user_id'];
    $res = $db->incLike($user_id,$id);
    echo $res;
    
    
} else {
    return "0";
}
?>