
<?php
require_once 'DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_GET['email']) && isset($_GET['password'])) {
 
    // receiving the post params
    $email = $_GET['email'];
    $password = $_GET['password'];
 
    // get the user by email and password
    $user = $db->getUserByEmailAndPassword($email, $password);
 
    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["user"]["id"] = $user["id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["type"] = $user["type"];
        
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>