
<?php

require_once __DIR__.'\DB_Functions.php';

$db = new DB_Functions();

 
// json response array

 
if (isset($_GET['id'])) {
    // receiving the post params
    $id = $_GET['id'];
    
    $res = $db->deleteUser($id);
    echo $res;
       
} else {
    return "0";
}
?>