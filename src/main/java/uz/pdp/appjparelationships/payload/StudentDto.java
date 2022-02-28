package uz.pdp.appjparelationships.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Integer id;

    private String firstName;

    private String lastName;

    private AddressDto addressDto;

    private GroupDto groupDto;

    private List<SubjectDto> subjectDtos;
}
