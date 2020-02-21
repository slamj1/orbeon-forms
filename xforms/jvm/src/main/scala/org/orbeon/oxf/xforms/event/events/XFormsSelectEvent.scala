/**
 * Copyright (C) 2010 Orbeon, Inc.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
 */
package org.orbeon.oxf.xforms.event.events

import org.orbeon.oxf.xforms.control.XFormsControl
import org.orbeon.oxf.xforms.event.XFormsEvent._
import org.orbeon.oxf.xforms.event.XFormsEvents._
import org.orbeon.oxf.xforms.event.{XFormsEvent, XFormsEventTarget}
import org.orbeon.oxf.xforms.itemset.Item
import org.orbeon.saxon.om

class XFormsSelectEvent(
  target     : XFormsEventTarget,
  properties : PropertyGetter
) extends XFormsUIEvent(
  XFORMS_SELECT,
  target.asInstanceOf[XFormsControl],
  properties,
  bubbles    = true,
  cancelable = false
) {

  import XFormsSelectEvent._

  def this(target: XFormsEventTarget, itemValue: Item.ItemValue[om.Item]) =
    this(target, Map(XFormsSelectEvent.ItemValueProperty -> Option(itemValue)))

  def this(target: XFormsEventTarget) = this(target, EmptyGetter)

  def itemValue: Item.ItemValue[om.Item] = property[Item.ItemValue[om.Item]](ItemValueProperty).get
}

private object XFormsSelectEvent {

  import XFormsEvent._

  val ItemValueProperty = xxfName("item-value")
}