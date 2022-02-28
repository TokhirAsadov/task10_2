package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.*;
import uz.pdp.appjparelationships.payload.*;
import uz.pdp.appjparelationships.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;


    @GetMapping("/allStudents")
    public List<StudentDto> getAllStudents(){
        return studentRepository.findAll().stream()
                .map(this::generateStudentDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/getStudentById/{id}")
    public StudentDto getStudentById(@PathVariable Integer id){
        Optional<Student> optional = studentRepository.findById(id);
        return optional.map(this::generateStudentDto).orElse(null);
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id){
        StudentDto dto = getStudentById(id);
        if (dto!=null) {
            studentRepository.deleteById(id);
            return "Student deleted successfully";
        }
        return "Student not found";
    }

    @PostMapping("/create")
    public String saveStudent(@RequestBody StudentDto dto){
        studentRepository.save(generateStudent(dto));
        return "Student saved successfully";
    }

    private Student generateStudent(StudentDto dto) {
        return new Student(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                generateAddress(dto.getAddressDto()),
                generateMyGroup(dto.getMyGroupDto()),
                dto.getSubjectDtos().stream().map(this::generateSubject).collect(Collectors.toList())
        );
    }

    private Subject generateSubject(SubjectDto dto) {
        return new Subject(
                dto.getId(),
                dto.getName()
        );
    }

    private Group generateMyGroup(MyGroupDto dto) {
        return new Group(
                dto.getId(),
                dto.getName(),
                generateFaculty(dto.getMyFacultyDto())
        );
    }

    private Faculty generateFaculty(MyFacultyDto dto) {
        return new Faculty(
                dto.getId(),
                dto.getName(),
                generateUniversity(dto.getMyUniversityDto())
        );
    }

    private University generateUniversity(MyUniversityDto dto) {
        return  new University(
                dto.getId(),
                dto.getName(),
                generateAddress(dto.getAddressDto())
        );
    }

    private Address generateAddress(AddressDto dto) {
        return new Address(
                dto.getId(),
                dto.getCity(),
                dto.getDistrict(),
                dto.getStreet()
        );
    }


    private StudentDto generateStudentDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                generateAddressDto(student.getAddress()),
                generateMyGroupDto(student.getGroup()),
                student.getSubjects().stream()
                        .map(this::generateSubjectDto)
                        .collect(Collectors.toList())
        );
    }

    private SubjectDto generateSubjectDto(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getName()
        );
    }

    private MyGroupDto generateMyGroupDto(Group group) {
        return new MyGroupDto(
                group.getId(),
                group.getName(),
                generateMyFacultyDto(group.getFaculty())
        );
    }

    private MyFacultyDto generateMyFacultyDto(Faculty faculty) {
        return new MyFacultyDto(
                faculty.getId(),
                faculty.getName(),
                generateMyUniversityDto(faculty.getUniversity())
        );
    }

    private MyUniversityDto generateMyUniversityDto(University university) {
        return new MyUniversityDto(
                university.getId(),
                university.getName(),
                generateAddressDto(university.getAddress())
        );
    }

    private AddressDto generateAddressDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getCity(),
                address.getDistrict(),
                address.getStreet()
        );
    }

    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

}
