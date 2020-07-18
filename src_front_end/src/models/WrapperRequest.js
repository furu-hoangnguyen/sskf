import FilterRequest from './FilterRequest';

export default class WrapperRequest {

  constructor (type, page, field, orderBy, limit) {

    this.type = type;

    this.page = page;

    this.field = field;

    this.orderBy = orderBy;

    this.limit = limit;

    this.filterRequest = new FilterRequest();
  }

}