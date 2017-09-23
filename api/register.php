
<?php

require_once __DIR__.'\DB_Functions.php';

$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_GET['name']) && isset($_GET['email']) && isset($_GET['password']) && isset($_GET['type'])) {
 
    // receiving the post params
    $name = $_GET['name'];
    $email = $_GET['email'];
    $password = $_GET['password'];
    $type = $_GET['type'];
    // check if user is already existed with the same email
    if ($db->isUserExisted($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($name, $email, $password,$type);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["id"] = $user["id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
           
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>