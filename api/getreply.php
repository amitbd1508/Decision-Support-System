
<?php
require_once __DIR__.'\DB_Functions.php';
if (isset($_GET['id'])){
    $id=$_GET['id'];
    $db = new DB_Functions();
    $db->getReplyByPostID($id);
}
?>