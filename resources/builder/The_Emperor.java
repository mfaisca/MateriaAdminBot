package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class The_Emperor extends _Default{
	public The_Emperor() {
		super("The Emperor", "emperor", "emperor");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9796};
				break;
			case HP:
				ids = new int[]{9799};
				break;
			case S1:
				ids = new int[]{6026};
				break;
			case S2:
				ids = new int[]{6040};
				break;
			case EX:
				ids = new int[]{6056};
				break;
			case AA:
				ids = new int[]{6065};
				break;
			case LD:
				ids = new int[]{9928};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}