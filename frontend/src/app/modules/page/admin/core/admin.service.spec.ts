import { ApiService } from '@core/api/api.service';
import { assembleContracts } from '@page/admin/core/admin.model';
import { TableHeaderSort } from '@shared/components/table/table.model';
import { ContractFilter, FilterOperator } from '@shared/model/filter.model';
import { of } from 'rxjs';
import { getContracts } from '../../../../mocks/services/admin-mock/admin.model';
import { AdminService } from './admin.service';

describe('AdminService', () => {
  let adminService: AdminService;
  let apiServiceMock: jasmine.SpyObj<ApiService>;

  beforeEach(() => {
    apiServiceMock = jasmine.createSpyObj('ApiService', [ 'getBy', 'post' ]);
    adminService = new AdminService(apiServiceMock);
  });

  it('should fetch distinct filter values', () => {
    const filterColumns = 'columnName';
    const searchElement = 'searchValue';
    const distinctValues = [ { value: 'value1' }, { value: 'value2' } ];
    apiServiceMock.getBy.and.returnValue(of(distinctValues));

    adminService.getDistinctFilterValues(filterColumns, searchElement).subscribe(result => {
      expect(result).toEqual(distinctValues);
    });

    expect(apiServiceMock.getBy).toHaveBeenCalledWith(`${ adminService.url }/contracts/distinctFilterValues`, jasmine.any(Object));
  });

  it('should get contracts with pagination and filtering', () => {
    const page = 1;
    const pageSize = 10;
    const sorting = [ [ 'columnName', 'asc' ] ] as TableHeaderSort[];
    const filter: ContractFilter = {
      id: {
        value: [ { value: 'value', strategy: FilterOperator.EQUAL } ],
        operator: 'OR',
      },
    };
    const responseData = { content: getContracts().content, page: 0, pageCount: 2, pageSize: 10, totalItems: 29 };
    apiServiceMock.post.and.returnValue(of(responseData));

    adminService.getContracts(page, pageSize, sorting, filter).subscribe(result => {
      expect(result.content).toEqual(assembleContracts(responseData.content));
      expect(result.pageCount).toEqual(responseData.pageCount);
      expect(result.totalItems).toEqual(responseData.totalItems);
    });

    expect(apiServiceMock.post).toHaveBeenCalledWith(`${ adminService.url }/contracts`, jasmine.any(Object));
    const requestBody = apiServiceMock.post.calls.mostRecent().args[1];
    expect(requestBody['page']).toEqual(page);
    expect(requestBody['size']).toEqual(pageSize);
    expect(requestBody['sorting']).toEqual(sorting);
    expect(requestBody['contractFilter']).toEqual(filter);
  });
});
