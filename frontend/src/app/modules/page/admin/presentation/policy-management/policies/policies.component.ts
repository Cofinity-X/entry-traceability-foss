import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Pagination } from '@core/model/pagination.model';
import { RoleService } from '@core/user/role.service';
import { KnownAdminRoutes } from '@page/admin/core/admin.model';
import { DeletionDialogComponent } from '@page/admin/presentation/policy-management/deletion-dialog/deletion-dialog.component';
import { PoliciesFacade } from '@page/admin/presentation/policy-management/policies/policies.facade';
import { Policy } from '@page/policies/model/policy.model';
import { TableType } from '@shared/components/multi-select-autocomplete/table-type.model';
import { TableSortingUtil } from '@shared/components/table/table-sorting.util';
import {
  CreateHeaderFromColumns,
  TableConfig,
  TableEventConfig,
  TableHeaderSort,
} from '@shared/components/table/table.model';
import { ToastService } from '@shared/components/toasts/toast.service';
import { setMultiSorting } from '@shared/helper/table-helper';
import { View } from '@shared/model/view.model';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-policies',
  templateUrl: './policies.component.html',
  styleUrls: [ './policies.component.scss' ],
})
export class PoliciesComponent {
  policiesView$: Observable<View<Pagination<Policy>>>;
  tableConfig: TableConfig;
  selectedPolicies: Policy[];
  policyFilter: any;
  pagination: TableEventConfig;
  multiSortList: TableHeaderSort[] = [];
  ctrlKeyState: boolean = false;

  constructor(public readonly policyFacade: PoliciesFacade, private readonly router: Router, private readonly toastService: ToastService, public dialog: MatDialog, private readonly roleService: RoleService) {
    window.addEventListener('keydown', (event) => {
      this.ctrlKeyState = setMultiSorting(event);
    });
    window.addEventListener('keyup', (event) => {
      this.ctrlKeyState = setMultiSorting(event);
    });
  }

  ngOnInit() {

    this.pagination = { page: 0, pageSize: 10, sorting: [ '', null ] };
    this.tableConfig = {
      displayedColumns: [ 'select', 'bpnSelection', 'policyName', 'policyId', 'accessType', 'createdOn', 'validUntil', 'constraints', 'menu' ],
      header: CreateHeaderFromColumns([ 'select', 'bpnSelection', 'policyName', 'policyId', 'accessType', 'createdOn', 'validUntil', 'constraints', 'menu' ], 'pageAdmin.policies'),
      menuActionsConfig: [ {
        label: 'actions.edit',
        icon: 'edit',
        action: (selectedPolicy: Record<string, unknown>) => this.openEditView(selectedPolicy),
        isAuthorized: this.roleService.isAdmin(),
      } ],
      sortableColumns: {
        select: false,
        bpnSelection: true,
        policyName: true,
        policyId: true,
        accessType: true,
        createdOn: true,
        validUntil: true,
        constraints: true,
        menu: false,
      },
      hasPagination: true,
    };

    this.policiesView$ = this.policyFacade.policies$;
    this.policiesView$.pipe(take(2)).subscribe(data => {
      if (data?.data?.content.length) {
        return;
      } else {
        this.policyFacade.setPolicies(0, 10);
      }
    });


  }

  filterActivated(policyFilter: any): void {
    this.policyFilter = policyFilter;
    this.policyFacade.setPolicies(this.pagination.page, this.pagination.pageSize, this.multiSortList, this.policyFilter);
  }

  onTableConfigChange(pagination: TableEventConfig): void {
    this.pagination = pagination;
    TableSortingUtil.setTableSortingList(pagination.sorting, this.multiSortList, this.ctrlKeyState);
    this.policyFacade.setPolicies(pagination.page, pagination.pageSize, this.multiSortList, this.policyFilter);
  }

  multiSelection(selectedPolicies: Policy[]) {
    this.selectedPolicies = selectedPolicies;
  }

  openDetailedView(selectedPolicy: Record<string, unknown>) {
    this.policyFacade.selectedPolicy = selectedPolicy as unknown as Policy;
    this.router.navigate([ 'admin/' + KnownAdminRoutes.POLICY_MANAGEMENT + '/' + this.policyFacade.selectedPolicy.policyId ]);
  }

  openEditView(selectedPolicy: any) {
    this.policyFacade.selectedPolicy = selectedPolicy as unknown as Policy;
    this.router.navigate([ 'admin/' + KnownAdminRoutes.POLICY_MANAGEMENT + '/edit/' + this.policyFacade.selectedPolicy.policyId ]);
  }

  openDeletionDialog() {
    const dialogRef = this.dialog.open(DeletionDialogComponent, {
      data: {
        policyIds: this.selectedPolicies.map(policy => policy.policyId),
        title: 'pageAdmin.policyManagement.policyDeletion',
      },
    });

    dialogRef.afterClosed().subscribe(confirmation => {
      if (confirmation) {
        this.deletePolicies();
      }
    });
  }


  deletePolicies() {
    this.policyFacade.deletePolicies(this.selectedPolicies).subscribe({
      next: value => {
        this.toastService.success('pageAdmin.policyManagement.deleteSuccess');
        this.policyFacade.setPolicies(this.pagination.page, this.pagination.pageSize, this.multiSortList, this.policyFilter);
      },
      error: err => {
        this.toastService.error('pageAdmin.policyManagement.deleteError');
      },
    });
  }


  protected readonly TableType = TableType;
}
