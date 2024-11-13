package experience.demo.Service;

import experience.demo.Model.Customer;
import experience.demo.Security.SecurityCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerService customerService;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer =  customerService.findCustomerByEmail(email);
        if(customer!=null){
            return new SecurityCustomer(customer.getEmail(),customer.getPassword());

        }else{
            throw new UsernameNotFoundException("User 404");
        }
    }

}
