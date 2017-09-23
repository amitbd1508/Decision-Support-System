<?php
 

class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password,$type) {
        
 
        $stmt = $this->conn->prepare("INSERT INTO tbluser( name, email, password,type) VALUES(?, ?, ?, ?)");
        $stmt->bind_param("ssss",$name, $email, $password,$type);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM tbluser WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tbluser WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            
            $server_password = $user['password'];
            
            // check for password equality
            if ($server_password == $password) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    public function getUserByEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tbluser WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
            
        } else {
            return NULL;
        }
    }
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from tbluser WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    
    public function getAllUser() {
 
        $stmt = $this->conn->prepare("SELECT * FROM tbluser");
 
        
 
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
	
	        print json_encode($rows);

            $stmt->close();
            
            
        } else {
            return NULL;
        }
    }
    public function deleteUser($id){

        $stmt = $this->conn->prepare("delete from tbluser where id=?");
        $stmt->bind_param("d",$id);
        $result = $stmt->execute();
        $stmt->close();
        if($result){
            return "1";
        } else {
            return "0";
        }
        
        
    }
    
    public function updateUser($id,$name, $email, $password,$type) {
        
 
        $stmt = $this->conn->prepare("update tbluser set name=?,email=? , password=?,type=? where id=?");
        $stmt->bind_param("ssssd",$name, $email, $password,$type,$id);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            return "1";
        } else {
            return "0";
        }
    }
    
    //==========================post handelling ======================
    
    public function insertPost($title,$body,$submittedby,$category){
        
        $now = new DateTime();
        $date=$now->format('Y-m-d H:i:s');
        $stmt = $this->conn->prepare("INSERT INTO tblpost(title,body,submittedby,category,date) VALUES(?, ?, ?, ?,?)");
        $stmt->bind_param("sssss",$title,$body,$submittedby,$category,$date);
        $result = $stmt->execute();
        $stmt->close();
        if($result){
            return "1";
        } else {
            return "0";
        }
    }
    
    public function incLike($user_id,$id){
        
        $db=new DB_Functions();
        if ($db->insertLikeDetails($user_id,$id,1)=="1");
        {
            $stmt = $this->conn->prepare("Update tblpost set likes= (select count(*) from tbllike where post_id=? ) where id=?");
            $stmt->bind_param("dd",$id,$id);
            $result = $stmt->execute();
            $stmt->close();
            if($result){
                return "1";
            } else {
                return "0";
            }
        }
    }
    
    public function decLike($user_id,$id){

        $db=new DB_Functions();
        if($db->insertLikeDetails($user_id,$id,0)==1)
        {
            $stmt = $this->conn->prepare("Update tblpost set likes= (select count(*) from tbllike where post_id=? ) where id=?");
            $stmt->bind_param("dd",$id,$id);
            $result = $stmt->execute();
            $stmt->close();
            if($result){
                return "1";
            } else {
                return "0";
            }
        }
        else return 0;
    }
    
    
    public function deletePost($post_id){

        $stmt = $this->conn->prepare("delete from tblpost where id=?");
        $stmt->bind_param("d",$post_id);
        $result = $stmt->execute();
        $stmt->close();
        if($result){
            return "1";
        } else {
            return "0";
        }
        
        
    }
    
    public function getPostByLikes() {
 
        $stmt = $this->conn->prepare("SELECT id,title, body,likes,submittedby ,category, date ,(select name from tbluser where id=submittedby) as username FROM tblpost  order by likes desc");
 
        
 
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
	
	        print json_encode($rows);

            $stmt->close();
            
            
        } else {
            return NULL;
        }
    }
    public function getPostByTop() {
 
        $stmt = $this->conn->prepare("SELECT * FROM tblpost  order by likes desc limit 3");
 
        
 
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
	
	        print json_encode($rows);

            $stmt->close();
            
            
        } else {
            return NULL;
        }
    }
    
    public function getPostByCategoryAdmin($category) {
 
        $stmt = $this->conn->prepare("SELECT id,title,body,likes,submittedby,category,date,(select name from tbluser where id=submittedby) as username  FROM tblpost  where category =? order by likes desc");
 
        
 
         $stmt->bind_param("s",$category);
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
	
	        print json_encode($rows);

            $stmt->close();
            
            
        } else {
            return NULL;
        }
    }
    public function getPostByCategory($category) {
 
        $stmt = $this->conn->prepare("SELECT id,title,body,likes,submittedby,category,date,(select name from tbluser where id=submittedby) as username  FROM tblpost  where category =? order by likes desc limit 3");
 
        
 
         $stmt->bind_param("s",$category);
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
	
	        print json_encode($rows);

            $stmt->close();
            
            
        } else {
            return NULL;
        }
    }
    //=============replay=========
     public function insertReplay($post_id,$reply){
        
        $stmt = $this->conn->prepare("INSERT INTO tblreplay(post_id,reply) VALUES(?, ?)");
        $stmt->bind_param("ds",$post_id,$reply);
        $result = $stmt->execute();
        $stmt->close();
        if($result){
            return "1";
        } else {
            return "0";
        }
    } 
    public function getLikes($id)
    {
        $stmt = $this->conn->prepare("SELECT user_id,( select name from tbluser where id=user_id ) as user FROM tbllike where post_id  =?");
 
        
 
         $stmt->bind_param("d",$id);
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
            $stmt->close();
	        return $rows;

            
            
            
        } else {
            return NULL;
        }
        
    }
    public function getReplyByPostID($id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tblreplay   where post_id =? order by id desc limit 1");
 
        
 
         $stmt->bind_param("d",$id);
        if ($stmt->execute()) {
            
            $rows = array();
            $result = $stmt->get_result();
           while($r = $result->fetch_assoc()) {
             $rows[] = $r;
           }
            
            $db=new DB_Functions();
            
            $response["likes"] = $db->getLikes($id);
            $response["reply"] = $rows;                  
	        print json_encode($response);
            $stmt->close();
            
            
        } else {
            return NULL;
        }
    }
    
    //===================liketable
    public function likeExist($user_id,$post_id){

        $stmt = $this->conn->prepare("select count(id) from tbllike where post_id=? and user_id= ?");
        $stmt->bind_param("dd",$post_id,$user_id);
        $result = $stmt->execute();
        
        $res= $stmt->get_result();
        $r=$res->fetch_assoc();
        $stmt->close();
        //var_dump($r);
        //echo $r[ 'count(id)']; 
        if($r[ 'count(id)']>=1){
            return "1";
        } else {
            return "0";
        }
    }
    public function insertLikeDetails($user_id,$post_id,$like){
        $db=new DB_Functions();
        
        if($db->likeExist($user_id,$post_id)=="1" && $like=="1")
            return "0";
        else{
        
            $q="";
            if($like=="1")
            {
                $q="INSERT INTO tbllike(post_id,user_id) VALUES(?, ?)";
            }
            else
            {
               $q="delete from tbllike where post_id=? and user_id=?";
            }

            $stmt = $this->conn->prepare($q);

            $stmt->bind_param("dd",$post_id,$user_id);
            $result = $stmt->execute();
            $stmt->close();
            if($result){
                return "1";
            } else {
                return "0";
            }
        }

        
    }
    
 
}
//$d=new DB_Functions();
//echo $d->deletePost(25);

?>