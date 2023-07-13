package net.wiringbits.spra.admin.utils.models

case class QueryParameters(sort: SortParameter, pagination: PaginationParameter, filters: List[FilterParameter]) {
  override def toString: String = {
    s"sort = ${sort.field} ${sort.ordering}, pagination = ${pagination.start} to ${pagination.end}, filters = ${filters
        .mkString("{", ", ", "}")}"
  }
}
