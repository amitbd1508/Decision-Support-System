
<?php

require_once __DIR__.'\DB_Functions.php';


if (isset($_GET['category'])){
    $category=$_GET['category'];
    $db = new DB_Functions();
    $db->getPostByCategory($category);
}

?>