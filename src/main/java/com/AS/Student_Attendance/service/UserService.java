<<<<<<< HEAD
package com.AS.Student_Attendance.service;

import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User getUserByUsername(String username){return userRepository.findByUsername(username);}
}
=======
package com.AS.Student_Attendance.service;


import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
>>>>>>> 3a66f6cf4adba815ff6b50b74fe7ced4c18eebe2
