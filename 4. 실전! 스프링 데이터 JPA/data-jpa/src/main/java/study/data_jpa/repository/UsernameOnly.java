package study.data_jpa.repository;

public interface UsernameOnly {

//    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
